package core.rmv.httpServer.http;

import com.google.common.cache.Cache;
import com.google.common.io.Resources;
import controller.Controller;
import core.serviceStation.ServiceType;
import core.serviceStation.dao.ServiceJDBCDAO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;


public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Cache<Long, String> dataCache;

    public HttpServerHandler(Cache<Long, String> dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Connected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // Check for invalid http data:
        if(msg.decoderResult() != DecoderResult.SUCCESS ) {
            ctx.close();
            System.out.println("closed");
            return;
        }

//        if (msg.uri().equals("/serviceStation")) {
//            serveStatic(ctx, "/index.html");
//        } else if (msg.uri().equals("/serviceStation/storeServiceData")) {
//            if (msg.method().equals(HttpMethod.OPTIONS)){
//                resolvePrefightedRequests(ctx, msg);
//            }
//            if (msg.method().equals(HttpMethod.POST)){
//                storeServiceData(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/getLastVehicleServiceRecord")) {
////            getVehicleServiceRecords(ctx, msg);
//            if (msg.method().equals(HttpMethod.OPTIONS)){
//                resolvePrefightedRequests(ctx, msg);
//            }
//            if (msg.method().equals(HttpMethod.POST)){
//                getLastVehicleServiceRecord(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/getServiceRecords")) {
////            getVehicleServiceRecords(ctx, msg);
//            if (msg.method().equals(HttpMethod.OPTIONS)){
//                resolvePrefightedRequests(ctx, msg);
//            }
//            if (msg.method().equals(HttpMethod.POST)){
//                getServiceRecords(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/getServiceRecordsPerVehicle")) {
//            if (msg.method().equals(HttpMethod.OPTIONS)){
//                resolvePrefightedRequests(ctx, msg);
//            }
//            if (msg.method().equals(HttpMethod.POST)){
//                getServiceRecordsPerVehicle(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/setServiceType")) {
//            if (msg.method().equals(HttpMethod.OPTIONS)){
//                resolvePrefightedRequests(ctx, msg);
//            }
//            if (msg.method().equals(HttpMethod.POST)){
//                setServiceTypes(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/getServiceTypes")) {
//            if (msg.method().equals(HttpMethod.OPTIONS)){
//                resolvePrefightedRequests(ctx, msg);
//            }
//            if (msg.method().equals(HttpMethod.POST)){
//                getServiceTypes(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/registerCustomer")) {
//            if (msg.method().equals(HttpMethod.POST)){
////                setServiceTypes(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/confirmTransaction")) {
//            if (msg.method().equals(HttpMethod.POST)){
//                sendTransactionConfirmation(ctx, msg);
//            }
//        }else if (msg.uri().equals("/serviceStation/requestAdditionalData")) {
//            if (msg.method().equals(HttpMethod.POST)){
//                requestAdditionalData(ctx, msg);
//            }
//        } else {
//            serveStatic(ctx, msg.uri());
//        }

    }

    private void storeServiceData(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException, UnsupportedEncodingException {
//        //decode request
//        ByteBuf data = msg.content();
//        int readableBytes = data.readableBytes();
//        String body = data.toString(StandardCharsets.UTF_8);
//
//
//        //convert the text to JSON object from here.
//        JSONObject jsonObject = new JSONObject(body);
//        System.out.println(jsonObject);
//
////        ServiceRecord serviceRecord = new ServiceRecord();
////        serviceRecord.setVehicle_id(jsonObject.getString("vehicleId"));
////        serviceRecord.setServiced_date(new Timestamp(System.currentTimeMillis()));
////        JSONArray services = jsonObject.getJSONArray("services");
////
////        for (int i = 0; i < services.length(); i++){
////            JSONObject serviceObject = (JSONObject) services.get(i);
////            int serviceId = serviceObject.getInt("serviceId");
////            JSONArray sparePartSerialNumber = serviceObject.getJSONArray("sparePartSerialNumber");
////            ArrayList<String> spareParts = new ArrayList<>();
////
////            if (sparePartSerialNumber.length()>0){
////                for (int k = 0; k < sparePartSerialNumber.length(); k++){
////                    spareParts.add((String) sparePartSerialNumber.get(k));
////                }
////            }
////            Service service = new Service(serviceId, spareParts);
////
//////            Service service = new Service(serviceId, sparePartSerialNumber, cost);
////
////            serviceRecord.setService(service);
////        }
////
////        ServiceJDBCDAO.getInstance().addServiceRecord(serviceRecord);
//
//        //writing response
//        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        ctx.write(response);
    }

    private void getLastVehicleServiceRecord(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException, UnsupportedEncodingException {
//        //decode request
//        ByteBuf data = msg.content();
//        int readableBytes = data.readableBytes();
//        String body = data.toString(StandardCharsets.UTF_8);
//        String result = java.net.URLDecoder.decode(body, "UTF-8");
//
//        JSONObject jsonObject = new JSONObject(result);
////        JSONObject jsonObject = new JSONObject(result.split("=")[1]);
//        System.out.println(jsonObject);
//
//        String vehicleNumber = jsonObject.getString("vehicleId");
//        String date = jsonObject.getString("date");
//
////        JSONArray vehicleData = ServiceJDBCDAO.getInstance().getAllServiceRecords(vehicleNumber);
//        JSONObject vehicleData = ServiceJDBCDAO.getInstance().getLastServiceRecord(vehicleNumber);
//        String stringVehicleData = vehicleData.toString();
//
//        writeResponse(ctx, stringVehicleData);
    }

    private void getServiceRecords(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
//        //decode request
//        ByteBuf data = msg.content();
//        int readableBytes = data.readableBytes();
//        String body = data.toString(StandardCharsets.UTF_8);
//
//        JSONObject jsonObject = new JSONObject(body);
//        System.out.println(jsonObject);
//        JSONArray vehicleData = ServiceJDBCDAO.getInstance().getAllServiceRecords();
//        String stringVehicleData = vehicleData.toString();
//
//        writeResponse(ctx, stringVehicleData);

    }

    private void getServiceRecordsPerVehicle(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
//        //decode request
//        ByteBuf data = msg.content();
//        String body = data.toString(StandardCharsets.UTF_8);
//
//        JSONObject jsonObject = new JSONObject(body);
//        System.out.println(jsonObject);
//        String vehicleNumber = jsonObject.getString("vehicleId");
//        JSONArray vehicleData = ServiceJDBCDAO.getInstance().getServiceRecordsPerVehicle(vehicleNumber);
//        String stringVehicleData = vehicleData.toString();
//
//        writeResponse(ctx, stringVehicleData);

    }

    private void setServiceTypes(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
//        //decode request
//        ByteBuf data = msg.content();
//        int readableBytes = data.readableBytes();
//        String body = data.toString(StandardCharsets.UTF_8);
//
//        //convert the text to JSON object from here.
//        JSONObject jsonObject = new JSONObject(body);
//        System.out.println(jsonObject);
//
//        String serviceType = jsonObject.getString("serviceType");
//
//        ServiceType serviceTypeObject = new ServiceType(serviceType);
//        ServiceJDBCDAO.getInstance().addServiceType(serviceTypeObject);
//
//        //writing response
//        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        ctx.write(response);
    }

    private void getServiceTypes(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
        JSONArray jsonArray = ServiceJDBCDAO.getInstance().getServiceTypes();
        String stringServiceTypes = jsonArray.toString();

        writeResponse(ctx, stringServiceTypes);
    }
    private void sendTransactionConfirmation(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
//        //decode request
//        ByteBuf data = msg.content();
//        int readableBytes = data.readableBytes();
//        String body = data.toString(StandardCharsets.UTF_8);
//
//        //convert the text to JSON object from here.
//        JSONObject jsonObject = new JSONObject(body);
//        System.out.println(jsonObject);
//
//        String blockHash = jsonObject.getString("blockHash");
//        Controller controller = new Controller();
//        controller.sendConfirmation(blockHash);
//
//        //writing response
//        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        ctx.write(response);
    }

    private void requestAdditionalData(ChannelHandlerContext ctx, FullHttpRequest msg){
//        //decode request
//        ByteBuf data = msg.content();
//        int readableBytes = data.readableBytes();
//        String body = data.toString(StandardCharsets.UTF_8);
//
//        //convert the text to JSON object from here.
//        JSONObject jsonObject = new JSONObject(body);
//        System.out.println(jsonObject);
//
//        String blockHash = jsonObject.getString("blockHash");
//        String sender = jsonObject.getString("sender");
//
//        Controller controller = new Controller();
//        controller.requestAdditionalData(blockHash, sender);
//
//        //writing response
//        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        ctx.write(response);
    }

    private void serveStatic(ChannelHandlerContext ctx, String path) throws Exception {
//        try {
//            byte[] raw = Resources.toByteArray(Resources.getResource(path.substring(1)));
//            ByteBuf content = Unpooled.wrappedBuffer(raw);
//
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html");
//            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
//            ctx.write(response);
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//            serve404(ctx);
//        }

    }

    private void serve404(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        ctx.write(response);
    }

    private void resolvePrefightedRequests(ChannelHandlerContext ctx, FullHttpRequest msg){
//        //writing response
//        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "POST");
//        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type");
////        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, "86400");
//        ctx.write(response);
    }

    private void writeResponse(ChannelHandlerContext ctx, String stringVehicleData){
//        try {
//            byte[] raw = stringVehicleData.getBytes(StandardCharsets.UTF_8);
//            ByteBuf content = Unpooled.wrappedBuffer(raw);
//
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//            ctx.write(response);
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//            serve404(ctx);
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}


