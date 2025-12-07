package nazario.nicos_backslots.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
//? >=1.19.3 {
/*import org.joml.Vector3d;
*///?} else {
import net.minecraft.client.util.math.Vector3d;
//?}

public class BackslotData {
    public static final BackslotData DEFAULT = new BackslotData(new Vector3d(1, 1, 1), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), "fixed");

    public Vector3d scale;
    public Vector3d offset;
    public Vector3d rotation;
    public String mode;

    public BackslotData(Vector3d scale, Vector3d offset, Vector3d rotation, String mode) {
        this.scale = scale;
        this.offset = offset;
        this.rotation = rotation;
        this.mode = mode;
    }

    private BackslotData(BackslotData data) {
        this.scale = new Vector3d(data.scale.x, data.scale.y, data.scale.z);
        this.offset = new Vector3d(data.offset.x, data.offset.y, data.offset.z);
        this.rotation = new Vector3d(data.rotation.x, data.rotation.y, data.rotation.z);
        this.mode = data.mode;
    }

    public static BackslotData fromJson(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();

        JsonObject scaleObject = object.getAsJsonObject("scale");
        Vector3d scale = new Vector3d(
                scaleObject.get("x").getAsFloat(),
                scaleObject.get("y").getAsFloat(),
                scaleObject.get("z").getAsFloat()
        );

        JsonObject offsetObject = object.getAsJsonObject("offset");
        Vector3d offset = new Vector3d(
                offsetObject.get("x").getAsFloat(),
                offsetObject.get("y").getAsFloat(),
                offsetObject.get("z").getAsFloat()
        );

        JsonObject rotationObject = object.getAsJsonObject("rotation");
        Vector3d rotation = new Vector3d(
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

    public BackslotData copy() {
        return new BackslotData(this);
    }
}
