package com.fastbiz.core.bootstrap.service.appserver;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.common.utils.FileUtils;
import com.fastbiz.core.bootstrap.service.appserver.ApplicationDescriptor.ApplicationType;

public class ApplicationScaner{

    private static Logger       LOG              = LoggerFactory.getLogger(ApplicationScaner.class);

    private static final String WAR_SUFFIX       = ".war";

    private static final String WEB_INF_FOLDER   = "WEB-INF";

    private static final String WEB_INF_FILE     = "web.xml";

    private static FileFilter   warFilter        = new WarFileFilter();

    private static FileFilter   webInfDirFilter  = new DirectoryHasFileFilter(WEB_INF_FOLDER, true);

    private static FileFilter   webInfFileFilter = new DirectoryHasFileFilter(WEB_INF_FILE, false);

    public List<ApplicationDescriptor> scan(File file, boolean isApplicationSet){
        List<ApplicationDescriptor> applications = new ArrayList<ApplicationDescriptor>();
        if (file == null || !file.exists()) {
            return applications;
        }
        if (isApplicationSet) {
            applications.addAll(scanSubFolders(file));
        } else {
            applications.add(scanFile(file));
        }
        return applications;
    }

    protected List<ApplicationDescriptor> scanSubFolders(File f){
        File[] dirs = f.listFiles();
        List<ApplicationDescriptor> applications = new ArrayList<ApplicationDescriptor>();
        for (File dir : dirs) {
            ApplicationDescriptor app = scanFile(dir);
            if (app != null) {
                applications.add(app);
            }
        }
        return applications;
    }

    ApplicationDescriptor createWarApplicationDescriptor(File applicationFile){
        ApplicationDescriptor descriptor = new ApplicationDescriptor();
        descriptor.setType(ApplicationType.STADARD_WAR);
        String name = FileUtils.getFilename(applicationFile.getName());
        descriptor.setPath("/" + name);
        descriptor.setName(name);
        descriptor.setDocBase(applicationFile.getAbsolutePath());
        return descriptor;
    }

    ApplicationDescriptor createFolderApplicationDescriptor(File applicationFile){
        ApplicationDescriptor descriptor = new ApplicationDescriptor();
        descriptor.setType(ApplicationType.STANDARD_FOLDER);
        String name = applicationFile.getName();
        descriptor.setPath("/" + name);
        descriptor.setName(name);
        descriptor.setDocBase(applicationFile.getAbsolutePath());
        return descriptor;
    }

    protected ApplicationDescriptor scanFile(File file){
        ApplicationDescriptor app = null;
        if (warFilter.accept(file)) {
            app = createWarApplicationDescriptor(file);
            if (app != null) {
                LOG.info("Found WAR application from location {}", file);
            }
            return app;
        } else if (webInfDirFilter.accept(file)) {
            File webInfDir = file.listFiles(new FilenameFilter(){

                public boolean accept(File dir, String name){
                    return WEB_INF_FOLDER.equals(name);
                }
            })[0];
            if (webInfFileFilter.accept(webInfDir)) {
                app = createFolderApplicationDescriptor(file);
                if (app != null) {
                    LOG.info("Found FOLDER application from location {}", file);
                }
            }
        }
        return app;
    }

    private static final class WarFileFilter implements FileFilter{

        public boolean accept(File file){
            if (file == null) {
                return false;
            }
            if (!file.exists()) {
                return false;
            }
            if (file.isFile() && file.getName().toLowerCase().endsWith(WAR_SUFFIX)) {
                return true;
            }
            return false;
        }
    }

    private static final class DirectoryHasFileFilter implements FileFilter{

        private String  normalFileName;

        private boolean isDirectory;

        public DirectoryHasFileFilter(String normalFileName, boolean isDirectory) {
            this.normalFileName = normalFileName;
            this.isDirectory = isDirectory;
        };

        @Override
        public boolean accept(File file){
            File[] children = file.listFiles(new FileFilter(){

                @Override
                public boolean accept(File f){
                    if (isDirectory) {
                        return f.isDirectory() && f.getName().equals(normalFileName);
                    } else {
                        return f.isFile() && f.getName().equals(normalFileName);
                    }
                }
            });
            return children != null && children.length == 1;
        }
    }
}
