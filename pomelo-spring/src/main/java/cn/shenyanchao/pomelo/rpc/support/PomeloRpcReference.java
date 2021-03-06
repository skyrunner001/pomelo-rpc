package cn.shenyanchao.pomelo.rpc.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import cn.shenyanchao.pomelo.rpc.discovery.DiscoveryModule;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcClientFactory;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler.PomeloClientProxy;

/**
 * @author shenyanchao
 */

public class PomeloRpcReference implements FactoryBean, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcReference.class);
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 序列化类型
     */
    private PomeloSerializer serializer;
    /**
     * 协议类型
     */
    private byte protocolType;
    /**
     * 组名
     */
    private String group;

    @Override
    public void destroy() throws Exception {
        DiscoveryModule.getInstance().close();
        PomeloRpcClientFactory.getInstance().stopClient();
    }

    @Override
    public Object getObject() throws Exception {
        return PomeloClientProxy.getInstance()
                .getProxyService(getObjectType(), timeout, serializer, protocolType, getObjectType().getName(),
                        group);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            if (StringUtils.isBlank(interfaceName)) {
                LOG.warn("interfaceName is null");
                return null;
            } else {
                return Thread.currentThread().getContextClassLoader().loadClass(interfaceName);
            }

        } catch (ClassNotFoundException e) {
            LOG.error("spring 解析失败", e);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public PomeloSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(PomeloSerializer serializer) {
        this.serializer = serializer;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

}
