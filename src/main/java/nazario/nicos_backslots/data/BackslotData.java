package nazario.nicos_backslots.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3d;

public class BackslotData {
    public static final BackslotData DEFAULT = new BackslotData(new Vec3d(1, 1, 1), Vec3d.ZERO, Vec3d.ZERO, "fixed");

    public Vec3d scale;
    public Vec3d offset;
    public Vec3d rotation;
    public String mode;

    public BackslotData(Vec3d scale, Vec3d offset, Vec3d rotation, String mode) {
        this.scale = scale;
        this.offset = offset;
        this.rotation = rotation;
        this.mode = mode;
    }

    public static BackslotData fromJson(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();

        JsonObject scaleObject = object.getAsJsonObject("scale");
        Vec3d scale = new Vec3d(
                scaleObject.get("x").getAsFloat(),
                scaleObject.get("y").getAsFloat(),
                scaleObject.get("z").getAsFloat()
        );

        JsonObject offsetObject = object.getAsJsonObject("offset");
        Vec3d offset = new Vec3d(
                offsetObject.get("x").getAsFloat(),
                offsetObject.get("y").getAsFloat(),
                offsetObject.get("z").getAsFloat()
        );

        JsonObject rotationObject = object.getAsJsonObject("rotation");
        Vec3d rotation = new Vec3d(
                rotationObject.get("x").getAsFloat(),
                rotationObject.get("y").getAsFloat(),
                rotationObject.get("z").getAsFloat()
        );

        return new BackslotData(
                scale,
                offset,
                rotation,
                object.get("mode").getAsString()
        );
    }
}
