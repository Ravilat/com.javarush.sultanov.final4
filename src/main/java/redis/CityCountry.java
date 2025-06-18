package redis;

import enums.Continent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class CityCountry {

    private Long id;

    private String name;

    private String district;

    private Long population;

    private String countryCode;

    private String alternativeCountryCode;

    private String countryName;

    private Continent continent;

    private String countryRegion;

    private BigDecimal countrySurfaceArea;

    private Long countryPopulation;

    private Set<LanguageRedis> countryLanguageRedis;

}
