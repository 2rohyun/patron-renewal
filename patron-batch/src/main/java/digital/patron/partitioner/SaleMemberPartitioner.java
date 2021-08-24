package digital.patron.partitioner;

import digital.patron.repository.SaleMemberRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class SaleMemberPartitioner implements Partitioner {

    private final SaleMemberRepository saleMemberRepository;

    public SaleMemberPartitioner(SaleMemberRepository saleMemberRepository) {
        this.saleMemberRepository = saleMemberRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long minId = saleMemberRepository.findMinId(); // 1
        long maxId = saleMemberRepository.findMaxId(); // 3,000

        long targetSize = (maxId - minId) / gridSize + 1; // 300

        // partition0 : 1 ~ 300
        // partition1 : 301 ~ 600
        // ..
        // partition9 : 2701 ~ 3000
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
