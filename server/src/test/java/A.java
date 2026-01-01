import com.dthvinh.Server.Models.Artist;

public class A {
    interface EventHandler<T>{
        void handle(String key, T val);
    }

    public static EventHandler<String> say(){
        return (key, message) -> {
            System.out.printf("Hello key: %s message: %s%n", key, message);
        };
    }

    public void run(String key,String val, EventHandler<String> handler){
        handler.handle(key,val);
    }

    public void main(){
        run("a", "b", A.say());
    }
}
