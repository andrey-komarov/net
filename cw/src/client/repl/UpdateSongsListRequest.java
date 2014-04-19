package client.repl;

import client.World;

public class UpdateSongsListRequest implements Request {
    @Override
    public void apply(World world) {
        world.updateSongsList();
    }
}
