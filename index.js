'use strict';

import { NativeModules, Platform } from 'react-native';

var CtsApiSer = {
    InitClient : function(type)
    {
        if(type=="Local")
        NativeModules.RNAndroidCtsApiService.InitHttpClientLocal();
        else
        NativeModules.RNAndroidCtsApiService.InitHttpClient();
    },
    GetToken :function(Host,Location,UserName,Password,CihazNo,KartNo)
    {
        return new Promise((fulfill, reject) => {
        
            NativeModules.RNAndroidCtsApiService.GetToken(Host,Location,UserName,Password,CihazNo,KartNo, (res,err) => {
                if (err) {
                    console.log(err);
                    reject(err);
                } 
                else {
                    console.log(res);
                    console.log("changing")
                    res.data = JSON.parse(res.bodyString);
                    res.text =  res.bodyString;
                    fulfill(res);
                }
    
            });
            
        });
},
GraphGet :function(Host,Location,Token,data)
{
    return new Promise((fulfill, reject) => {
    
        console.log(Host);
        console.log(Location);
        console.log(Token);
        console.log(data);
        console.log(JSON.stringify(data));
        NativeModules.RNAndroidCtsApiService.GraphGet(Host,Location,JSON.stringify(data),Token,  (res,err ) => {
            if (err) {
                console.log(err);
                reject(err);
            } 
            else {
                console.log(res);
                res.data = JSON.parse(res.bodyString);
                res.text =  res.bodyString;
                fulfill(res);
            }

        });
        
    });
}
,
PostValue :function(Host,Location,Token,data)
{
    return new Promise((fulfill, reject) => {
    
        NativeModules.RNAndroidCtsApiService.PostValue(Host,Location,JSON.stringify(data), Token,  (res,err) => {
            if (err) {
                console.log(err);
                reject(err);
            } 
            else {
                console.log(res);
                res.data = JSON.parse(res.bodyString);
                    res.text =  res.bodyString;
                    fulfill(res);
            }

        });
        
    });
}

};


module.exports = CtsApiSer;
