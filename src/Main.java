import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Commands commands = new Commands(args[0],args[1],args[2]);
        commands.readInitials();
        commands.doCommands();
    }
}