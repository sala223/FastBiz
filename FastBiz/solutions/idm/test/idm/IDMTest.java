package test.idm;

import com.fastbiz.core.solution.SID;
import com.fastbiz.core.tenant.TenantHolder;
import com.fastbiz.core.testsuite.SolutionTests;

@SID("idm")
public class IDMTest extends SolutionTests{

    static {
        TenantHolder.setTenant("test");
    }
}
