package pl.lodz.p.it.ssbd2024.ssbd03.config;

import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/metrics")
public class MetricController {

    private final PrometheusMeterRegistry registry;

    @PostConstruct
    public void init() {
        new JvmThreadMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new DiskSpaceMetrics(new File("/")).bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new UptimeMetrics().bindTo(registry);
    }

    @GetMapping
    ResponseEntity<?> getMetrics() {
        return ResponseEntity.ok(registry.scrape());
    }
}
