package com.company.ydomain;

import com.company.ydomain.external.YApiPort;
import com.company.ydomain.external.YDto;

class YApiPortImpl implements YApiPort {
    YdomainService svc = new YdomainService();
    
    @Override
    public YDto getY() {
        return svc.getY();
    }
}
