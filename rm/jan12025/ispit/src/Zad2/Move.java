package Zad2;

public enum Move{
    ROCK("rock"),
    PAPER("paper"),
    SCISSORS("scissors");
    
    private final String displayName;
    
    Move(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public boolean beats(Move other) {
        return (this == ROCK && other == SCISSORS) ||
                (this == SCISSORS && other == PAPER) ||
                (this == PAPER && other == ROCK);
    }

    public static Move getRandomMove(){
        Move[] moves = Move.values();
        return moves[(int)(Math.random()*moves.length)];
    }
}
