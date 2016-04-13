# ruisParsers
json/xml parser , ormore!


* [json example]

json text:
###test json text
{"e": {"desc": "","provider": "play","code": 0},"data": {"id": 381858926,"stream": [{"logo": "none","segs": [{"key": "73458f4978ce6025261ef3a2","size": "27196325"}],"width": 640}]}}
###

```Java
String neirong= "";//上面的json内容
Youku_beans info=new Youku_beans();
ruisJsonParsers<Youku_beans> parser=new ruisJsonParsers<Youku_beans>(info);
parser.parse(neirong);
System.out.println(info.getData().get(0).getId()+"|"+info.getData().get(0).getStream().get(0).getSegs().get(0).getKey());
```

###output:
381858926|c3eae93a3c93b596282b5570

