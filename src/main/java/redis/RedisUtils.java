package redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.City;
import entity.Country;
import entity.Language;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class RedisUtils {

    RedisClient redisClient;
    ObjectMapper objectMapper;

    public RedisUtils(RedisClient redisClient) {
       this.redisClient = redisClient;
       objectMapper = new ObjectMapper();
    }

    public void pushToRedis(List<CityCountry> preparedData) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : preparedData) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), objectMapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readDataFromRedis(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    objectMapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<CityCountry> transformData(List<City> cityList) {
        return cityList.stream().map(new Function<City, CityCountry>() {
            @Override
            public CityCountry apply(City city) {
                CityCountry cityCountry = new CityCountry();

                cityCountry.setId(city.getId());
                cityCountry.setName(city.getName());
                cityCountry.setDistrict(city.getDistrict());
                cityCountry.setPopulation(city.getPopulation());

                Country country = city.getCountry();

                cityCountry.setCountryCode(country.getCode());
                cityCountry.setAlternativeCountryCode(country.getCode2());
                cityCountry.setCountryName(country.getName());
                cityCountry.setContinent(country.getContinent());
                cityCountry.setCountryRegion(country.getRegion());
                cityCountry.setCountrySurfaceArea(country.getSurfaceArea());
                cityCountry.setCountryPopulation(country.getPopulation());

                Set<Language> languages = country.getLanguages();

                Set<LanguageRedis> languagesRedis = languages.stream().map(language -> {
                    LanguageRedis languageRedis = new LanguageRedis();
                    languageRedis.setLanguage(language.getLanguage());
                    languageRedis.setIsOfficial(language.getIsOfficial());
                    languageRedis.setPercentage(language.getPercentage());
                    return languageRedis;
                }).collect(Collectors.toSet());

                cityCountry.setCountryLanguageRedis(languagesRedis);
                return cityCountry;
            }
        }).collect(Collectors.toList());
    }

}
