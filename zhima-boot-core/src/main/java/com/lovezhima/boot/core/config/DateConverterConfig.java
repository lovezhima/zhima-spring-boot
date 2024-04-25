package com.lovezhima.boot.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.lovezhima.boot.core.exception.CommonRuntimeException;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author king
 */
@Configuration
public class DateConverterConfig {

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";


    static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(@NonNull String source) {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        }
    }

    static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(@NonNull String source) {
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
        }
    }

    static class StringToLocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(@NonNull String source) {
            return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
        }
    }

    static class StringToDateConverter implements Converter<String, Date> {
        @Override
        public Date convert(@NonNull String source) {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
            try {
                return format.parse(source);
            } catch (ParseException e) {
                throw new CommonRuntimeException(e);
            }
        }
    }

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new StringToLocalDateConverter();
    }
//

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new StringToLocalDateTimeConverter();
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new StringToLocalTimeConverter();
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new StringToDateConverter();
    }


    /**
     * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        // 序列化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        // 反序列化
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));


        //Date序列化和反序列化
        dateSerializerOrDeserializer(javaTimeModule, DEFAULT_DATE_TIME_FORMAT);
        dateSerializerOrDeserializer(javaTimeModule, DEFAULT_DATE_FORMAT);

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    private void dateSerializerOrDeserializer(JavaTimeModule javaTimeModule, String defaultDateFormat) {
        dateSerializer(javaTimeModule, defaultDateFormat);
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<>() {
            @Override
            public Date deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws IOException {
                SimpleDateFormat format = new SimpleDateFormat(defaultDateFormat);
                String date = jsonParser.getText();
                try {
                    return format.parse(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void dateSerializer(JavaTimeModule javaTimeModule, String defaultDateFormat) {
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                SimpleDateFormat formatter = new SimpleDateFormat(defaultDateFormat);
                String formattedDate = formatter.format(date);
                jsonGenerator.writeString(formattedDate);
            }
        });
    }
}
