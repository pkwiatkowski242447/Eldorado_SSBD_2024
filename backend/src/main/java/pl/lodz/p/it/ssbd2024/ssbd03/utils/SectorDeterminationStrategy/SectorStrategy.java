package pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorDeterminationStrategy;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.List;

public interface SectorStrategy {
    public Sector choose(List<Sector> sectors);
}
