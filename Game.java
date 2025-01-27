import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private static final String Game_Locations= """
            road,at the end of the road, W: hill, E:well house,S:valley,N:forest
            hill,on top of hill with a view in all directions,N:forest, E:road
            well house,inside a well house for a small spring,W:road,N:lake,S:stream
            valley,in a forest valley beside a tumbling stream,N:road,W:hill,E:stream
            forest,at the edge of a thick dark forest,S:road,E:lake
            lake,by an alpine lake surrounded by wildflowers,W:forest,S:well house
            stream,near a stream with a rocky bed,W:valley, N:well house
            """;
    private enum Compass{
        E,N,S,W;
        private static final String[] directions={"East","North","South","West"};
        public String getString(){
            return directions[this.ordinal()];
        }
    }
    private record Locations(String description, Map<Compass,String> nextPlaces){

    }
    private String lastPlace;
    private Map<String,Locations> adventureMap=new HashMap<>();

    public Game() {
        this(null);
    }

    public Game(String customLocations) {
        loadLocations(Game_Locations);
        if(customLocations!=null) loadLocations(customLocations);
    }
    private void loadLocations(String data){
        for(String s:data.split("\\R")){
            String[] parts=s.split(",",3);
            Arrays.asList(parts).replaceAll(String::trim);
            Map<Compass,String> nextPlaces=loadDirections(parts[2]);
            Locations locations=new Locations(parts[1], nextPlaces);
            adventureMap.put(parts[0],locations);
        }
    }

    private Map<Game.Compass, String> loadDirections(String nextPlaces) {
        Map<Compass,String> directions=new HashMap<>();
        List<String> nextSteps=Arrays.asList(nextPlaces.split(","));
        nextSteps.replaceAll(String::trim);
        for(String nextPlace: nextSteps){
            String[] splits=nextPlace.split(":");
            Compass compass=Compass.valueOf(splits[0].trim());
            String destination=splits[1].trim();
            directions.put(compass,destination);
        }
        return directions;
    }
    public void visit(Locations location){
        System.out.printf("*** You are Standing %s *** %n",location.description());
        System.out.println("\tFrom here, you can see: ");
        location.nextPlaces.forEach((k,v)-> {
            System.out.printf("\t-> A %s to the %s (%s) %n",v,k.getString(),k);
        });
        System.out.print("Select Your Compass (Q to quit) >> ");
    }
    public void move(String direction){
        var nextPlaces=adventureMap.get(lastPlace).nextPlaces;
        String nextPlace =null;
        if("ENSW".contains(direction)){
            nextPlace=nextPlaces.get(Compass.valueOf(direction));
            if(nextPlace!=null) play(nextPlace);
        }
        else if(direction.equals("Q")) System.out.println("Thankyou for playing! Exiting..........");
        else System.out.println("!! Invalid Directions. please try again !!");
    }

    public void play(String location) {
        if(adventureMap.containsKey(location)){
            Locations next=adventureMap.get(location);
            lastPlace=location;
            visit(next);
        }else System.out.println(location+" is an invalid location");
    }
}
