package net.sf.jcommon.ws;

import com.fasterxml.jackson.databind.ObjectMapper;  
  



import java.io.IOException;  
import java.io.Reader;  
import java.io.Writer;  
  
import javax.websocket.DecodeException;  
import javax.websocket.Decoder;  
import javax.websocket.EncodeException;  
import javax.websocket.Encoder;  
import javax.websocket.EndpointConfig;  

import net.sf.jcommon.util.ReflectUtils;
  
/**
 * public class XCoder extends JsonCoder<X> {}
 * 
 * @ServerEndpoint(value="/hello", encoders = {XCoder.class}, decoders = {XCoder.class})  
 * 
 * 
 * @param <T>
 */
public abstract class JsonCoder<T> implements Encoder.TextStream<T>, Decoder.TextStream<T>{  
  
    private Class<T> type;  
      
    // When configured my read in that ObjectMapper is not thread safe  
    private ThreadLocal<ObjectMapper> mapper = new ThreadLocal<ObjectMapper>() {  
        @Override  
        protected ObjectMapper initialValue() {  
            return new ObjectMapper();  
        }  
    };  
 
    @SuppressWarnings("unchecked")
	@Override  
    public void init(EndpointConfig endpointConfig) {  
        type = (Class<T>) ReflectUtils.getActualType(getClass(), JsonCoder.class, "T");
    }  
  
    @Override  
    public void encode(T object, Writer writer) throws EncodeException, IOException {  
        mapper.get().writeValue(writer, object);  
    }  
  
    @Override  
    public T decode(Reader reader) throws DecodeException, IOException {  
        return mapper.get().readValue(reader, type);  
    }  
  
    @Override  
    public void destroy() {            
    }
}  
