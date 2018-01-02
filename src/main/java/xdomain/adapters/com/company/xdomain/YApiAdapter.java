package com.company.xdomain;

import com.company.ydomain.external.YApiPort;

class YApiAdapter {
    YApiPort yapiPort;

    public  String getY() {
        return yapiPort.getY().value;
    }
}
