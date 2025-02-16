package nazario.backslot.data;

import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BackslotDataLoader implements IdentifiableResourceReloadListener {

    // Global data storage for your backslot data
    public static final Map<Identifier, BackslotData> DATA = new HashMap<>();

    @Override
    public Identifier getFabricId() {
        return new Identifier("backslot", "backslot_data");
    }

    @Override
    public CompletableFuture<Void> reload(
            ResourceReloader.Synchronizer synchronizer,
            ResourceManager manager,
            Profiler prepareProfiler,
            Profiler applyProfiler,
            Executor prepareExecutor,
            Executor applyExecutor) {
        // Prepare stage: load and parse all JSON files asynchronously.
        CompletableFuture<Map<Identifier, BackslotData>> prepareFuture = CompletableFuture.supplyAsync(() -> {
            Map<Identifier, BackslotData> preparedData = new HashMap<>();

            // Look for JSON files in the "backslot_data" folder.
            manager.findResources("backslot_data", id -> id.toString().endsWith(".json"))
                    .forEach((id, resource) -> {
                        try (InputStream stream = resource.getInputStream();
                             InputStreamReader reader = new InputStreamReader(stream)) {

                            String[] cleanId = id.getPath().replace("backslot_data/", "").replace(".json","").split("/");

                            Identifier key = new Identifier(cleanId[0], cleanId[1]);

                            preparedData.put(key, BackslotData.fromJson(JsonParser.parseReader(reader)));
                            System.out.println("Loaded scaling config from " + id);
                        } catch (Exception e) {
                            System.err.println("Error loading scaling config " + id + ": " + e.getMessage());
                        }
                    });
            return preparedData;
        }, prepareExecutor);

        // Synchronize the prepared data and then apply it on the main thread.
        return prepareFuture
                .thenCompose(synchronizer::whenPrepared)
                .thenAcceptAsync(preparedData -> {
                    // Apply stage: update your global data storage.
                    DATA.clear();
                    DATA.putAll(preparedData);
                }, applyExecutor);
    }
}