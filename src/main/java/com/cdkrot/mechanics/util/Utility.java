package com.cdkrot.mechanics.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.cdkrot.mechanics.Mechanics;

public class Utility {
    // a BIG utility class
    private static java.lang.reflect.Method EntityFallMethod;

    static {
        try {
            EntityFallMethod = Entity.class.getDeclaredMethod("fall", float.class);
            EntityFallMethod.setAccessible(true);
        } catch (Exception e) {
            try {
                EntityFallMethod = Entity.class.getDeclaredMethod("func_70069_a", float.class);
                EntityFallMethod.setAccessible(true);
            } catch (Exception ex) {
                throw new RuntimeException("FATAL INIT EXCEPTION", ex);
            }
        }
    }

    /**
     * This will return suggested metadata(Side) for block which placed on side
     */
    // IDE reports that this method never used, however keeping it for future.
    public static int getMetadataForBlockSidePlaced(float rotationYaw) {
        // int l = MathHelper.floor_double((double)((rotationYaw * 4F) / 360F) +
        // 0.5D) & 3;
        int l = MathHelper.floor_double((double) (rotationYaw / 90F) + 0.5D) & 3;// four values

        // if (l == 0)
        // return 2;
        if (l == 1)
            return 5;
        if (l == 2)
            return 3;
        if (l == 3)
            return 4;
        return 2;
    }

    /**
     * This will return suggested metadata(Side) for block which placed on any
     * side Mostly copypaste of BlockPistonBase.determineOrientation
     */
    public static int getMetadataForBlockAnyPlaced(int x, int y, int z, EntityLivingBase entity) {
        if (MathHelper.abs((float) entity.posX - (float) x) < 2.0F && MathHelper.abs((float) entity.posZ - (float) z) < 2.0F) {
            double d0 = entity.posY + 1.82D - (double) entity.yOffset;

            if (d0 - (double) y > 2.0D) {
                return 1;
            }

            if ((double) y - d0 > 0.0D) {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    /**
     * if use veci3 as a x,y,z container it can be required to swap components
     * to get components of one vec strict more than of other. Performes the
     * routine
     * 
     * @param tomin
     *            vec getting min
     * @param tomax
     *            vec getting max
     */
    public static void SwapVectorsComponentsi(VecI3 tomin, VecI3 tomax) {
        int t;
        if (tomin.x > tomax.x) {
            t = tomin.x;
            tomin.x = tomax.x;
            tomax.x = t;
        }
        if (tomin.y > tomax.y) {
            t = tomin.y;
            tomin.y = tomax.y;
            tomax.y = t;
        }
        if (tomin.z > tomax.z) {
            t = tomin.z;
            tomin.z = tomax.z;
            tomax.z = t;
        }
    }

    public static AxisAlignedBB SelectPoolBasingOnVectorAndInc(VecI3 base, VecI3Base dirvec) {
		return AxisAlignedBB.getBoundingBox(base.x+dirvec.x, base.y+dirvec.y, base.z+dirvec.z,
				base.x+dirvec.x+1, base.y+dirvec.y+1, base.z+dirvec.z+1);
       /*if (DirectionalVecs.isFacingNegative(dirvec))
            return AxisAlignedBB.getBoundingBox(base.x + dirvec.x, base.y + dirvec.y, base.z + dirvec.z, base.x, base.y, base.z);
        else
            return AxisAlignedBB.getBoundingBox(base.x, base.y, base.z, base.x + dirvec.x, base.y + dirvec.y, base.z + dirvec.z);*/
    }

    public static int getDefaultDirectionsMeta(World world, int x, int y, int z) {
        boolean front = world.getBlock(x, y, z - 1).isOpaqueCube();
        boolean back = world.getBlock(x, y, z + 1).isOpaqueCube();
        boolean left = world.getBlock(x - 1, y, z).isOpaqueCube();
        boolean right = world.getBlock(x + 1, y, z).isOpaqueCube();
        int meta = 3;

        // if (front && !back) meta = 3; //logic: this assignment makes no sense

        if (back && !front)
            meta = 2;

        if (left && !right)
            meta = 5;

        if (right && !left)
            meta = 4;

        return meta;
    }

    // TODO: warning, IDE reports that this method never used.
    public static VecD3 vecFromEntity(Entity e) {
        return new VecD3(e.posX, e.posY, e.posZ);
    }

    public static void doEntityFall(Entity e) {
        try {
            EntityFallMethod.invoke(e, e.fallDistance);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // TODO: warning, IDE reports that this method never used.
    public static EnumFacing getDirectionVectorInVanillaFormat(int side) {
        return EnumFacing.getFront(side);
    }

    /**
     * Loads File as String array.
     * 
     * @param file
     *            path to file as in classloader.loadresource
     * @param encoding
     *            file encoding
     * @return String array containing file's text or null if fails.
     */
    public static String[] loadFileAsStringArray(String file, String encoding) {
        InputStream in = Mechanics.class.getClassLoader().getResourceAsStream(file);
        if (in == null) {
            Mechanics.modLogger.warn("[FLoader] Failed loading file " + file + " with encoding " + encoding);
            return null;
        }
        List<String> list = new ArrayList<String>();
        Scanner s = new Scanner(in, encoding);
        if (s.hasNext())// at least 1 line
            s.nextLine();// first line is skipped because of possible encoding problem
        while (s.hasNextLine()) {
            String l = s.nextLine();
            if (l.startsWith("#") || l.equals(""))
                continue;// ignored
            list.add(l);
        }
        s.close();
        System.out.println("load scs" + list.size());
        return list.toArray(new String[list.size()]);
    }

    public static Map<String, String> loadKeyValueMap(InputStream input) {
        Map<String, String> map = new HashMap<String, String>();
        if (input == null)
            return null;
        Scanner scanner = new Scanner(input, "UTF-16");
        if (scanner.hasNext())// at least 1 line
            scanner.nextLine();// first line is skipped because of encoding problem
        while (scanner.hasNextLine()) {
            String l = scanner.nextLine();
            if (l.startsWith("#") || l.equals(""))
                continue;// ignored
            String parts[] = l.split("::", 2);
            if (parts.length < 2)
                Mechanics.modLogger.warn("[Key-Value Loader] Bad line in kv file: " + l);
            else if (!map.containsKey(parts[0]))
                map.put(parts[0], parts[1]);
            else
                Mechanics.modLogger.warn("[Key-Value Loader] Key allready registered: " + parts[0] + ".");
        }
        scanner.close();
        return map;
    }

    /**
     * Returns random object from list or null, if empty
     */
    public static <T> T randomFromList(List<T> list, Random r) {
        return list.size() == 0 ? null : list.get(r.nextInt(list.size()));
    }

    // note that some of code is unused, it was used before, and (maybe) will be needed in the future.
}