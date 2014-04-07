package lab3.repl;

import lab3.main.World;

import java.io.File;

public class PutFileRequest implements Request {
    public final File file;

    public PutFileRequest(File file) {
        this.file = file;
    }

    @Override
    public void apply(World world) {
        world.registerNewFile(file);
    }
}
