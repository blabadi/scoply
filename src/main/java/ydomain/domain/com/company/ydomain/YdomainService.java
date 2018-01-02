package com.company.ydomain;

import com.company.ydomain.external.YDto;

class YdomainService {
    DatabaseAdapter dbAdapter = new DatabaseAdapter();

    public YDto getY(){
        YDto y = new YDto();
        y.value = "Y";
        return y;
    }
}
