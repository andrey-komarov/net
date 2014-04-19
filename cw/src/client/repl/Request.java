package client.repl;


import client.World;

public interface Request {
    void apply(World world);
}
