package core.serviceStation.webSocketServer.webSocket;

import core.blockchain.Block;
import core.connection.HistoryDAO;
import core.consensus.Consensus;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseCombiner;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

/**
 * @author ameya
 */
public class WebSocketMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> implements Observer {

    Consensus consensus = Consensus.getInstance();

    public WebSocketMessageHandler() {
        super(false);
//        Consensus con = Consensus.getInstance();
        consensus.addObserver(this);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    private static final ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

//    private static final Channel chnnl = new Defa

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            final String text = ((TextWebSocketFrame) frame).text();
            LOGGER.info("Received text frame {}", text);

            //convert the text to JSON object from here.
            JSONObject jsonObject = new JSONObject(text);
            System.out.println(jsonObject);

            PromiseCombiner promiseCombiner = new PromiseCombiner();
            allChannels.stream()
                    .filter(c -> c != ctx.channel())
                    //.forEach(c -> c.writeAndFlush(frame.copy()));
                    .forEach(c -> {
                        frame.retain();
                        promiseCombiner.add(c.writeAndFlush(frame.duplicate()).addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (!future.isSuccess()) {
                                    LOGGER.info("Failed to write to channel: {}", future.cause());
                                }
                            }
                        }));
                    });

            Promise aggPromise = ctx.newPromise();
            promiseCombiner.finish(aggPromise);

            aggPromise.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (frame.release()) {
                        LOGGER.debug("WebSocket frame successfully deallocated");
                    } else {
                        LOGGER.warn("WebSocket frame leaked!");
                    }
                }
            });

        } else {
            throw new UnsupportedOperationException("Invalid websocket frame received");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Adding new channel {} to list of channels", ctx.channel().remoteAddress());
//        allChannels.close();
        allChannels.add(ctx.channel());
    }

    @Override
    public void update(Observable o, Object arg) {
        PromiseCombiner promiseCombiner = new PromiseCombiner();

        TextWebSocketFrame tws = new TextWebSocketFrame("Hi Sajinie");

        allChannels.stream()
            .forEach(c -> {
                promiseCombiner.add(c.writeAndFlush(tws).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            LOGGER.info("Failed to write to channel: {}", future.cause());
                        }
                    }
                }));
            });
    }

    public static void addBlockToNotificationArray(Block nonApprovedBlock) {
        JSONObject jsonBlock = Block.getBlockRepresentation(nonApprovedBlock);
        jsonBlock.put("action", "addBlockToNotifications");
        String stringBlock = jsonBlock.toString();

        PromiseCombiner promiseCombiner = new PromiseCombiner();
        TextWebSocketFrame tws = new TextWebSocketFrame(stringBlock);

        allChannels.stream()
                .forEach(c -> {
                    promiseCombiner.add(c.writeAndFlush(tws).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                LOGGER.info("Failed to write to channel: {}", future.cause());
                            }
                        }
                    }));
                });
    }

    public static void removeBlockFromNotificationArray(Block block) {
        JSONObject jsonBlock = Block.getBlockRepresentation(block);
        jsonBlock.put("action", "removeBlockFromNotifications");
        String stringBlock = jsonBlock.toString();

        PromiseCombiner promiseCombiner = new PromiseCombiner();
        TextWebSocketFrame tws = new TextWebSocketFrame(stringBlock);

        allChannels.stream()
                .forEach(c -> {
                    promiseCombiner.add(c.writeAndFlush(tws).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                LOGGER.info("Failed to write to channel: {}", future.cause());
                            }
                        }
                    }));
                });
    }

    public static void giveAdditionalData(String blockHash, String dataRequester) throws SQLException {
        HistoryDAO historyDAO = new HistoryDAO();
        JSONObject jsonObject = historyDAO.getBlockData(blockHash);

        jsonObject.put("action", "additionalDataRequest");
        jsonObject.put("requester", dataRequester);
        String blockInfo = jsonObject.toString();

        PromiseCombiner promiseCombiner = new PromiseCombiner();
        TextWebSocketFrame tws = new TextWebSocketFrame(blockInfo);

        allChannels.stream()
                .forEach(c -> {
                    promiseCombiner.add(c.writeAndFlush(tws).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                LOGGER.info("Failed to write to channel: {}", future.cause());
                            }
                        }
                    }));
                });
    }
}
