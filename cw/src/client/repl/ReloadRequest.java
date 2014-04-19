package client.repl;

import client.World;

/**
 * Created by andrey on 4/19/14.
 */
public class ReloadRequest implements Request {
    @Override
    public void apply(World world) {
        world.sendReload();
    }
}
