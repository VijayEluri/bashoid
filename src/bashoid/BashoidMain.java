package bashoid;


public class BashoidMain {

    public static void main(String[] args) throws Exception {
        Bashoid bot = new Bashoid();
        bot.connect("irc.rizon.net");
        bot.joinChannel("#abraka");
    }
}
