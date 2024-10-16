package org.youcode.DevSync.Scheduler;

import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TokenManagerDAOImpl;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.TokenManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TokenGrantScheduler {

    private static final Logger logger = Logger.getLogger(TokenGrantScheduler.class.getName());

    private final RequestDAOImpl requestDAO;
    private final TokenManagerDAOImpl tokenManagerDAO;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Constructor with DAOs
    public TokenGrantScheduler(RequestDAOImpl requestDAO, TokenManagerDAOImpl tokenManagerDAO) {
        this.requestDAO = new RequestDAOImpl();
        this.tokenManagerDAO = new TokenManagerDAOImpl();
        startScheduler();
    }

    // Start the scheduler to run periodically
    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndGrantTokens, 0, 1, TimeUnit.MINUTES);
    }

    // Check and grant tokens based on the defined criteria
    private void checkAndGrantTokens() {
        try {
            logger.info("checkAndGrantTokens method executed at: " + LocalDateTime.now());
            List<Request> requests = requestDAO.findAllPendingRequests();
            logger.info("Pending requests found: " + requests.size());

            for (Request request : requests) {
                Long requestId = request.getId();
                logger.info("Processing request ID: " + requestId);
                grantTokensIfEligible(requestId);
            }
        } catch (Exception e) {
            logger.severe("Error in checkAndGrantTokens: " + e.getMessage());
        }
    }



    // Grant tokens if the request is eligible
    public void grantTokensIfEligible(Long requestId) {
        Request request = requestDAO.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (LocalDateTime.now().isAfter(request.getResponseDeadline()) && !request.isTokensGranted()) {
            Optional<TokenManager> tokenManagerOpt = tokenManagerDAO.findByUserId(request.getUser().getId());

            if (tokenManagerOpt.isPresent()) {
                TokenManager tokenManager = tokenManagerOpt.get();
                tokenManager.doubleTokens(); // Assuming this method updates tokens
                tokenManagerDAO.update(tokenManager);
            } else {
                TokenManager newTokenManager = new TokenManager();
                newTokenManager.setUser(request.getUser());
                newTokenManager.setDailyTokens(4); // Set initial tokens
                tokenManagerDAO.save(newTokenManager);
            }

            request.setTokensGranted(true);
            requestDAO.update(request);
        }
    }

    // Method to trigger manual checks (if needed)
    public void triggerManualCheck() {
        checkAndGrantTokens();
    }

    // Clean up resources if needed
    public void stopScheduler() {
        scheduler.shutdown();
    }
}
