package nio.service;

import com.georgeinfo.ginkgo.injection.annotation.Service;

/**
 * @author George (GeorgeWorld@qq.com)
 */
@Service
public class TestRPCServiceImpl implements TestRPCService {
    public String getName(int n) {
        return "周润发：" + n;
    }
}
