package core.connection;

public class Identity {
    private String block_hash;
    private String role;
    private String name;

    public Identity(String block_hash, String role, String name){
        this.block_hash = block_hash;
        this.role = role;
        this.name = name;
    }

    public String getBlock_hash() {
        return block_hash;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
