package io.github.xiaoyureed.demospringbootstarter;

/**
 * @author xiaoyu
 * date: 2020/3/17
 */
public class Demo2ServiceImpl implements IService {

    private final IServiceProperties iServiceProperties;

    public Demo2ServiceImpl(IServiceProperties iServiceProperties) {
        this.iServiceProperties = iServiceProperties;
    }

    @Override
    public String getName() {
        return iServiceProperties.getName();
    }

    @Override
    public void exec() {
        System.out.println(">>> hello, " + this.getName());
    }
}
