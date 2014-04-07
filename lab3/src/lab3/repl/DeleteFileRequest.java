package lab3.repl;

import lab3.main.World;

import java.io.File;

public class DeleteFileRequest implements Request {
    public final File file;

    public DeleteFileRequest(File file) {
        this.file = file;
    }

    @Override
    public void apply(World world) {
        world.unregisterFile(file);
    }
}
