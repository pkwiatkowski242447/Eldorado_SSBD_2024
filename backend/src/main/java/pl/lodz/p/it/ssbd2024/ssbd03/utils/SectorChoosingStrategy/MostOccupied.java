package pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorChoosingStrategy;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.Comparator;
import java.util.List;

public class MostOccupied implements SectorStrategy{

    @Override
    public Sector choose(List<Sector> sectors) {
        return sectors.stream().max(Comparator.comparingInt(s -> s.getMaxPlaces() - s.getAvailablePlaces())).orElse(null);
    }
}
