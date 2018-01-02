package com.company.xdomain;

class XdomainService {
    YApiAdapter yAdapter;

    public String getXY(){
        return "X" + yAdapter.getY();
    }
}
