package no.sparebank1.sb1fs.fs;

import jnr.ffi.Platform;
import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Sb1fs extends FuseStubFS {
	private static final Logger log = LoggerFactory.getLogger(Sb1fs.class);
	
	private final Node root;
	private final String mountPath;


	public Sb1fs(final Node root, String mountPath) {
		super();
		this.root = root;
		this.mountPath = mountPath;

		log.debug("Loaded fs-tree: {}", root.getFilesystemTree());
	}


	public void mount() {
        mount(Paths.get(mountPath), false, false);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
        }));

        // Keep the main process alive on windows (press any key to terminate)
        if (Platform.OS.WINDOWS.equals(Platform.getNativePlatform().getOS())) {
            try {
                System.err.println("===== Server Running - press any key to terminate =====");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        log.info("Unmounting sb1fs at : {}",mountPath);
        umount();
        log.info("Successfully unmounted sb1fs at : {}",mountPath);
    }

    @Override
    public void mount(Path mountPoint, boolean blocking, boolean debug) {
        log.info("Mounting sb1fs at : {}",mountPath);
        super.mount(mountPoint, blocking, debug);
        log.info("Successfully mounted sb1fs at : {}",mountPath);
    }
	
	@Override
    public int getattr(String path, FileStat stat) {
        log.debug("getattr({})",path);
        Node node =root.resolve(path);
        if (node == null) return -ErrorCodes.ENOENT();
        stat.st_mtim.tv_sec.set(node.modifiedTimeStampMillis()/1000);
        stat.st_mtim.tv_nsec.set((node.modifiedTimeStampMillis()%1000)*1000);
        stat.st_uid.set(getContext().uid.get());
        stat.st_gid.set(getContext().gid.get());
        if (node.isDir()) {
        	stat.st_mode.set(FileStat.S_IFDIR | 0755);
        }
        else {
        	stat.st_mode.set(FileStat.S_IFREG | 0444);
            stat.st_size.set(node.getBytes().length);
        }       
        return 0;
    }

    @Override
    public int readdir(String path, Pointer buf, FuseFillDir filter, @off_t long offset, FuseFileInfo fi) {
        log.debug("readdir({})",path);
        Node node = root.resolve(path);



        if (node == null) return -ErrorCodes.ENOENT();
        if (!node.isDir()) return -ErrorCodes.ENOTDIR();

        filter.apply(buf, ".", null, 0);
        filter.apply(buf, "..", null, 0);

        for(Node child : node.getSubNodes()) {
            log.debug("readdir: adding child: {}, isdir: {}",child.getName(),child.isDir());
        	filter.apply(buf, child.getName(), null, 0);
        }
        
        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        log.debug("open({})",path);
        return 0;
    }

    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset, FuseFileInfo fi) {
        log.debug("read({})",path);
        Node node = root.resolve(path);
        if (node == null) return -ErrorCodes.ENOENT();
   	 	if (node.isDir()) return -ErrorCodes.EISDIR();;

        byte[] bytes = node.getBytes();
        int length = bytes.length;
        if (offset < length) {
            if (offset + size > length) {
                size = length - offset;
            }
            buf.put(0, bytes, 0, bytes.length);
        } else {
            size = 0;
        }
        return (int) size;
    }

    @Override
    public int readlink(String path, Pointer buf, long size) {
	    log.debug("readlink({})",path);
        return super.readlink(path, buf, size);
    }

    @Override
    public int mknod(String path, long mode, long rdev) {
        log.debug("mknod({})",path);
        return super.mknod(path, mode, rdev);
    }

    @Override
    public int mkdir(String path, long mode) {
        log.debug("mkdir({})",path);
        return super.mkdir(path, mode);
    }

    @Override
    public int unlink(String path) {
        log.debug("unlink({})",path);
        return super.unlink(path);
    }

    @Override
    public int rmdir(String path) {
        log.debug("rmdir({})",path);
        return super.rmdir(path);
    }

    @Override
    public int symlink(String oldpath, String newpath) {
        log.debug("symlink({})",oldpath);
        return super.symlink(oldpath, newpath);
    }

    @Override
    public int rename(String oldpath, String newpath) {
        log.debug("rename({})",oldpath);
        return super.rename(oldpath, newpath);
    }

    @Override
    public int link(String oldpath, String newpath) {
        log.debug("link({})",oldpath);
        return super.link(oldpath, newpath);
    }

    @Override
    public int chmod(String path, long mode) {
        log.debug("chmod({})",path);
        return super.chmod(path, mode);
    }

    @Override
    public int chown(String path, long uid, long gid) {
        log.debug("chown({})",path);
        return super.chown(path, uid, gid);
    }

    @Override
    public int truncate(String path, long size) {
        log.debug("truncate({})",path);
        return super.truncate(path, size);
    }

    @Override
    public int write(String path, Pointer buf, long size, long offset, FuseFileInfo fi) {
        log.debug("write({})",path);
        return super.write(path, buf, size, offset, fi);
    }

    @Override
    public int statfs(String path, Statvfs stbuf) {
        log.debug("statfs({})",path);
        return super.statfs(path, stbuf);
    }

    @Override
    public int flush(String path, FuseFileInfo fi) {
        log.debug("flush({})",path);
        return super.flush(path, fi);
    }

    @Override
    public int release(String path, FuseFileInfo fi) {
        log.debug("release({})",path);
        return super.release(path, fi);
    }

    @Override
    public int fsync(String path, int isdatasync, FuseFileInfo fi) {
        log.debug("fsync({})",path);
        return super.fsync(path, isdatasync, fi);
    }

    @Override
    public int setxattr(String path, String name, Pointer value, long size, int flags) {
        log.debug("setxattr({})",path);
        return super.setxattr(path, name, value, size, flags);
    }

    @Override
    public int getxattr(String path, String name, Pointer value, long size) {
        log.debug("getxattr({})",path);
        return super.getxattr(path, name, value, size);
    }

    @Override
    public int listxattr(String path, Pointer list, long size) {
        log.debug("listxattr({})",path);
        return super.listxattr(path, list, size);
    }

    @Override
    public int removexattr(String path, String name) {
        log.debug("removexattr({})",path);
        return super.removexattr(path, name);
    }

    @Override
    public int opendir(String path, FuseFileInfo fi) {
        log.debug("opendir({})",path);
        return super.opendir(path, fi);
    }

    @Override
    public int releasedir(String path, FuseFileInfo fi) {
        log.debug("releasedir({})",path);
        return super.releasedir(path, fi);
    }

    @Override
    public int fsyncdir(String path, FuseFileInfo fi) {
        log.debug("fsyncdir({})",path);
        return super.fsyncdir(path, fi);
    }

    @Override
    public Pointer init(Pointer conn) {
        log.debug("init({})",conn);
        return super.init(conn);
    }

    @Override
    public void destroy(Pointer initResult) {
        log.debug("destroy({})",initResult);
        super.destroy(initResult);
    }

    @Override
    public int access(String path, int mask) {
        log.debug("access({})",path);
        return super.access(path, mask);
    }

    @Override
    public int create(String path, long mode, FuseFileInfo fi) {
        log.debug("create({})",path);
        return super.create(path, mode, fi);
    }

    @Override
    public int ftruncate(String path, long size, FuseFileInfo fi) {
        log.debug("ftruncate({})",path);
        return super.ftruncate(path, size, fi);
    }

    @Override
    public int fgetattr(String path, FileStat stbuf, FuseFileInfo fi) {
        log.debug("fgetattr({})",path);
        return super.fgetattr(path, stbuf, fi);
    }

    @Override
    public int lock(String path, FuseFileInfo fi, int cmd, Flock flock) {
        log.debug("lock({})",path);
        return super.lock(path, fi, cmd, flock);
    }

    @Override
    public int utimens(String path, Timespec[] timespec) {
        log.debug("utimens({})",path);
        return super.utimens(path, timespec);
    }

    @Override
    public int bmap(String path, long blocksize, long idx) {
        log.debug("bmap({})",path);
        return super.bmap(path, blocksize, idx);
    }

    @Override
    public int ioctl(String path, int cmd, Pointer arg, FuseFileInfo fi, long flags, Pointer data) {
        log.debug("ioctl({})",path);
        return super.ioctl(path, cmd, arg, fi, flags, data);
    }

    @Override
    public int poll(String path, FuseFileInfo fi, FusePollhandle ph, Pointer reventsp) {
        log.debug("poll({})",path);
        return super.poll(path, fi, ph, reventsp);
    }

    @Override
    public int write_buf(String path, FuseBufvec buf, long off, FuseFileInfo fi) {
        log.debug("write_buf({})",path);
        return super.write_buf(path, buf, off, fi);
    }

    @Override
    public int read_buf(String path, Pointer bufp, long size, long off, FuseFileInfo fi) {
        log.debug("read_buf({})",path);
        return super.read_buf(path, bufp, size, off, fi);
    }

    @Override
    public int flock(String path, FuseFileInfo fi, int op) {
        log.debug("flock({})",path);
        return super.flock(path, fi, op);
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, FuseFileInfo fi) {
        log.debug("fallocate({})",path);
        return super.fallocate(path, mode, off, length, fi);
    }

    @Override
    public FuseContext getContext() {
        log.debug("getContext()");
        return super.getContext();
    }

    @Override
    protected String getFSName() {
        log.debug("getFSName()");
        return super.getFSName();
    }

}
