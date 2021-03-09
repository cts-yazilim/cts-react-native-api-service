'use strict';

import { NativeModules, Platform } from 'react-native';
var Q = require('q');

var CtsApiSer = {
    GetToken :function(Host,Location,UserName,Password,CihazNo)
    {
        return new Promise((fulfill, reject) => {
        
            NativeModules.RNAndroidCtsApiService.GetToken(Host,Location,UserName,Password,CihazNo, (res,err) => {
                if (err) {
                    console.log(err);
                    reject(err);
                } 
                else {
                    console.log(res);
                    res.json = JSON.parse(res.bodyString);
                    res.text =  res.bodyString;
                    fulfill(res);
                }
    
            });
            
        });
},
GraphGet :function(Host,Location,Token,data)
{
    return new Promise((fulfill, reject) => {
    
        NativeModules.RNAndroidCtsApiService.GraphGet(Host,Location,JSON.stringify(data),Token,  (res,err ) => {
            if (err) {
                console.log(err);
                reject(err);
            } 
            else {
                console.log(res);
                res.json = JSON.parse(res.bodyString);
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
                res.json = JSON.parse(res.bodyString);
                    res.text =  res.bodyString;
                    fulfill(res);
            }

        });
        
    });
}

};


module.exports = CtsApiSer;
