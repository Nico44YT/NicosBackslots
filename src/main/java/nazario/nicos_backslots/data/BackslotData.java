package nazario.nicos_backslots.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.Vec3f;

public class BackslotData {
    public static final BackslotData DEFAULT = new BackslotData(new Vec3f(1, 1, 1), Vec3f.ZERO, Vec3f.ZERO, "fixed");

    public Vec3f scale;
    public Vec3f offset;
    public Vec3f rotation;
    public String mode;

    public BackslotData(Vec3f scale, Vec3f offset, Vec3f rotation, String mode) {
        this.scale = scale;
        this.offset = offset;
        this.rotation = rotation;
        this.mode = mode;
    }

    public static BackslotData fromJson(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();

        JsonObject scaleObject = object.getAsJsonObject("scale");
        Vec3f scale = new Vec3f(
                scaleObject.get("x").getAsFloat(),
                scaleObject.get("y").getAsFloat(),
                scaleObject.get("z").getAsFloat()
        );

        JsonObject offsetObject = object.getAsJsonObject("offset");
        Vec3f offset = new Vec3f(
                offsetObject.get("x").getAsFloat(),
                offsetObject.get("y").getAsFloat(),
                offsetObject.get("z").getAsFloat()
        );

        JsonObject rotationObject = object.getAsJsonObject("rotation");
        Vec3f rotation = new Vec3f(
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
