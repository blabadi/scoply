package com.company.xdomain;

import com.company.xdomain.external.XApiPort;

class XApiPortImpl implements XApiPort {
    XdomainService svc = new XdomainService();

    @Override
    public String getXY() {
        return svc.getXY();
    }
}
