package net.novelmc.util.nbt;

import com.comphenix.net.sf.cglib.proxy.Factory;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtWrapper;
import net.novelmc.util.ConverseBase;
import net.novelmc.util.Reflect;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MiniFactory extends ConverseBase {
    private static Method m;
    private static boolean fuzzy = false;

    private static final Reflect reflect;

    static {
        reflect = new Reflect(com.comphenix.protocol.injector.server.TemporaryPlayer.class);

        try {
            m = NbtFactory.class.getDeclaredMethod("getStackModifier", ItemStack.class);
            m.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            plugin.getLogger().severe(ExceptionUtils.getStackTrace(e));
        }

        try {
            Class.forName(reflect.getDefClass().getName());
            fuzzy = true;
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().severe(ExceptionUtils.getStackTrace(ex));
        }
    }

    public static NbtWrapper<?> fromItemTag(ItemStack stack) {
        StructureModifier<NbtBase<?>> modifier = null;
        try {
            modifier = (StructureModifier<NbtBase<?>>) m.invoke(null, stack);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            plugin.getLogger().severe(ExceptionUtils.getStackTrace(e));
        }
        NbtBase<?> result = modifier.read(0);
        if (result != null && result.toString().contains("{\"name\": \"null\"}")) {
            modifier.write(0, null);
            result = modifier.read(0);
        }

        if (result == null) {
            return null;
        }

        return NbtFactory.fromBase(result);
    }

    public static NbtWrapper<?> fromBlockTag(Block block) {
        StructureModifier<NbtBase<?>> modifier = null;
        try {
            modifier = (StructureModifier<NbtBase<?>>) m.invoke(null, block);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            plugin.getLogger().severe(ExceptionUtils.getStackTrace(e));
        }
        NbtBase<?> result = modifier.read(0);
        if (result != null && result.toString().contains("{\"name\": \"null\"}")) {
            modifier.write(0, null);
            result = modifier.read(0);
        }

        if (result == null) {
            return null;
        }

        return NbtFactory.fromBase(result);
    }

    @Nullable
    public static Player getPacketPlayer(@NotNull PacketEvent event) {
        Player e = event.getPlayer();
        Player b;
        String name;

        if (e == null || !e.isOnline()) return null;
        if (fuzzy && event.isPlayerTemporary()) return null;
        name = e.getName();
        if (name == null) return null;
        b = Bukkit.getPlayerExact(name);
        if (b == null || !b.isOnline() || (b instanceof Factory)) return null;
        return b;
    }

    public static Reflect reflector() {
        return reflect;
    }


}
