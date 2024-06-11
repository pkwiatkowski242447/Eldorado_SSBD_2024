package pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorDeterminationStrategy;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.Comparator;
import java.util.List;

public class MostOccupied implements SectorStrategy{

    @Override
    public Sector choose(List<Sector> sectors) {
        return sectors.stream().max(Comparator.comparingInt(Sector::getOccupiedPlaces)).orElse(null);
    }
}
