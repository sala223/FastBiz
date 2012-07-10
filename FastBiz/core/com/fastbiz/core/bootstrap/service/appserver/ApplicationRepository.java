package com.fastbiz.core.bootstrap.service.appserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fastbiz.common.structure.Pair;

public final class ApplicationRepository{

    private ApplicationScaner           scanner      = new ApplicationScaner();

    private List<ApplicationDescriptor> applications = new ArrayList<ApplicationDescriptor>();

    private List<Pair<File, Boolean>>   locations    = new ArrayList<Pair<File, Boolean>>();

    public void addApplication(File file){
        locations.add(new Pair<File, Boolean>(file, false));
    }

    public void addApplicationSet(File folder){
        locations.add(new Pair<File, Boolean>(folder, true));
    }

    public void scan(){
        for (Pair<File, Boolean> p : locations) {
            applications.addAll(scanner.scan(p.getFirst(), p.getSecond()));
        }
    }

    public List<ApplicationDescriptor> getApplications(){
        return applications;
    }
}
