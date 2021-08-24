package digital.patron.partitioner;

import digital.patron.repository.BusinessMemberRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class BusinessMemberPartitioner implements Partitioner {

    private final BusinessMemberRepository businessMemberRepository;

    public BusinessMemberPartitioner(BusinessMemberRepository businessMemberRepository) {
        this.businessMemberRepository = businessMemberRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long minId = businessMemberRepository.findMinId(); // 1
        long maxId = businessMemberRepository.findMaxId(); // 3,000

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
