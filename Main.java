import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game=new Game();
        game.play("road");
        Scanner scanner=new Scanner(System.in);
        boolean playing=true;
        while(playing){
            String direction=scanner.next().trim().toUpperCase().substring(0,1);
            if(direction.equals("Q")) playing=false;
            game.move(direction);
        }
        scanner.close();
    }
}
