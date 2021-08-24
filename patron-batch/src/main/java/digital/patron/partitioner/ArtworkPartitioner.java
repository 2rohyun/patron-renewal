package digital.patron.partitioner;

import digital.patron.repository.ArtworkRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ArtworkPartitioner implements Partitioner {

    private final ArtworkRepository artworkRepository;

    public ArtworkPartitioner(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long minId = artworkRepository.findMinId(); // 1
        long maxId = artworkRepository.findMaxId(); // 6,000

        long targetSize = (maxId - minId) / gridSize + 1; // 600

        // partition0 : 1 ~ 600
        // partition1 : 601 ~ 1200
        // ..
        // partition9 : 5401 ~ 6000
        Map<String, ExecutionContext> result = new HashMap<>();

        long number = 0;
        long start = minId;
        long end = start + targetSize -1;
        while(start <= maxId) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= maxId) {
                end = maxId;
            }

            value.putLong("minId", start);
            value.putLong("maxId", end);

            start += targetSize;
            end += targetSize;
            number++;
        }

        return result;
    }
}
