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


###Youku_beans class
```Java
import java.io.Serializable;
import java.util.List;

public class Youku_beans implements Serializable {
	private List<Youku_beans.data> data;
	
	public List<Youku_beans.data> getData() {
		return data;
	}

	public void setData(List<Youku_beans.data> data) {
		this.data = data;
	}

	public static class data implements Serializable{
		private long id;
		private List<Youku_beans.stream> stream;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public List<Youku_beans.stream> getStream() {
			return stream;
		}
		public void setStream(List<Youku_beans.stream> stream) {
			this.stream = stream;
		}
	}

	public static class stream implements Serializable{
		private List<Youku_beans.segs> segs;

		public List<Youku_beans.segs> getSegs() {
			return segs;
		}

		public void setSegs(List<Youku_beans.segs> segs) {
			this.segs = segs;
		}
	}

	public static class segs implements Serializable{
		private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
}
```
