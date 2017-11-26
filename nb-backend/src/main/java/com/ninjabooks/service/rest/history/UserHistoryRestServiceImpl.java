package com.ninjabooks.service.rest.history;

import com.ninjabooks.domain.History;
import com.ninjabooks.domain.User;
import com.ninjabooks.json.history.UserHistoryResponse;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class UserHistoryRestServiceImpl implements HistoryRestService
{
    private static final Logger logger = LogManager.getLogger(UserHistoryRestServiceImpl.class);

    private static final LocalDate TODAY_DATE = LocalDate.now();

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserHistoryRestServiceImpl(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserHistoryResponse> getHistory(long minusDaysFromToday, Long entityID) {
        logger.info("An axttempt to find user history, user id: {}", entityID);

        User user = EntityUtils.getEnity(userService, entityID);
        Stream<History> userHistoryStream = user.getHistories().parallelStream();
        userHistoryStream = minusDaysFromToday > 0 ?
            filterHistoriesByDate(userHistoryStream, minusDaysFromToday) :
            userHistoryStream;

        return userHistoryStream
            .map(history -> new UserHistoryResponse(history, modelMapper))
            .collect(Collectors.toList());
    }

    private Stream<History> filterHistoriesByDate(Stream<History> historyStream, final long minusDaysFromToday) {
        final LocalDate date = TODAY_DATE.minusDays(minusDaysFromToday);
        return historyStream.filter(history -> {
            LocalDate returnDate = history.getReturnDate();
            return !returnDate.isBefore(date) && !returnDate.isAfter(TODAY_DATE);
        });
    }

}
