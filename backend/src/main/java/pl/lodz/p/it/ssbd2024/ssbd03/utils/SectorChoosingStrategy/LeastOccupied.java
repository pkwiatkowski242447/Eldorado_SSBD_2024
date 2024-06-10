package pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorChoosingStrategy;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.Comparator;
import java.util.List;

public class LeastOccupied implements SectorStrategy{
    @Override
    public Sector choose(List<Sector> sectors) {
        return sectors.stream().min(Comparator.comparingInt(s -> s.getMaxPlaces() - s.getAvailablePlaces())).orElse(null);
    }
}
