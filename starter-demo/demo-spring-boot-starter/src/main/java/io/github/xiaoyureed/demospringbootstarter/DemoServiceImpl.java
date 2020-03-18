package io.github.xiaoyureed.demospringbootstarter;

/**
 * @author xiaoyu
 * date: 2020/3/16
 */
public class DemoServiceImpl implements IService {

    private final IServiceProperties iServiceProperties;

    public DemoServiceImpl(IServiceProperties iServiceProperties) {
        this.iServiceProperties = iServiceProperties;
    }


    @Override
    public void exec() {
        System.out.println(">>> hello, " + getName());
    }

    @Override
    public String getName() {
        return iServiceProperties.getName();
    }
}
