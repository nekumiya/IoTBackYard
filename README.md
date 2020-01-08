![IotBackYard](http://visualdust.com/kexie/repository/IotBackYard/Images/Head.png)

 ![Language-java](https://img.shields.io/badge/Language-java_kotlin-orange) 
 [![VisualDust](https://img.shields.io/badge/Assignment-VisualDust-darkgreen)](https://github.com/VisualDust) 
 [![](https://img.shields.io/badge/Email-VisualDust%40outlook.com-green)](VisualDust@outlook.com)
 ![License-MIT](https://img.shields.io/badge/License-MIT-blue) 

 ---

 # IoTBackYard:痛击友军的物联网后端  

> 队友们注意了！  
* 目前处于测试阶段。测试方法 :  

 对于物联网终端 :   
使用MQTT协议发布消息到`tcp:/mqtt.visualdust.com`,主题为id/test。(id为包裹id)消息数据类型为字符串。格式为:  
"分隔符,包裹ID,包裹名称,扩展key01,扩展value01,扩key02,扩展value02,扩展key03,扩展value03......"(不括双引号)  
样例: 有一个包裹,id为3a4b7c9d-3710-409a-aa8c-327396a7f88e,名称为'一个裹的名称',它的包裹内温度为20度,它的重量为5。基站应将它编成由逗号分隔的字符串,并且在主题3a4b7c9d-3710-409a-aa8c-327396a7f88e/test下发布这样一条消息:  
",,3a4b7c9d-3710-409a-aa8c-327396a7f88e,一个包的名称,temprature,20,weight,5"(不包括双引号)  
当然,这条信息可以变得更长。服务器在接收到这条信息后，除了id和名称会单独保管以外，会将后面不定长的扩展信息储存在一张哈希表中。一个包裹的任何扩展信息都可以使用相应的key取得。并且，扩展信息可以乱序。  
注:这里的id推荐使用UUID国际标准生成。详情百科UUID。  
当你发送了这条消息后，服务器将在主题id/ServerSideCallBack下(其中id为你发送的信息的id)发布一条内容为"Package received procedure complete"来表示服务器收到并解析了你的消息，测试成功。  
当然，每个包裹的ID应该不一样。所以针对每个不同的包裹ID服务端只处理一次。再次发送需要换一个包裹ID。
