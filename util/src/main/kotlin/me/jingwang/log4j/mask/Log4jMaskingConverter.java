package me.jingwang.log4j.mask;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import java.util.Arrays;

@Plugin(name = "logmasker", category = PatternConverter.CATEGORY)
@ConverterKeys({"msk", "mask"})
public class Log4jMaskingConverter extends LogEventPatternConverter {
    private MaskingConverter maskingConverter;

    private Log4jMaskingConverter(MaskingConverter maskingConverter) {
        super("Log4jMaskingConverter", null);
        this.maskingConverter = maskingConverter;
    }

    public static Log4jMaskingConverter newInstance(String[] options) {
        MaskingConverter maskingConverter = new MaskingConverter();
        if (options == null) {
            maskingConverter.init(null);
        } else {
            maskingConverter.init(Arrays.asList(options));
        }
        return new Log4jMaskingConverter(maskingConverter);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        StringBuilder logMessage = new StringBuilder(event.getMessage().getFormattedMessage());
        maskingConverter.mask(logMessage);
        System.out.println(logMessage);
        toAppendTo.append(logMessage);
    }

}
