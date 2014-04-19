package client.repl;

import client.World;

import java.io.File;

public class PlaySongRequest implements Request {
    public final int pos;

    public PlaySongRequest(int pos) {
        this.pos = pos;
    }

    @Override
    public void apply(World world) {
        world.playSong(pos);
    }
}
