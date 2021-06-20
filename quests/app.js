const fastify = require('fastify')({
    logger: true
})

fastify.register(require('fastify-mongodb'), {
    // force to close the mongodb connection when app stopped
    // the default value is false
    forceClose: true,

    url: 'mongodb://admin:9216328Ww@185.117.152.68:27017/ruba?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false'
})
//var mongoose = require('mongoose');

//mongoose.connect('mongodb://admin:9216328Ww@185.117.152.68:27017/ruba?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false');
//var defunctSchema = mongoose.Schema({
    //name: String,
   // surname: String,
   // otch: String,
   // birthday: Date,
   // death: Date,
   // grave: String,
   // area: String,
  //  cemetery: String,
   // location: String,
  //  id: Number,
//});
fastify.get('/defunct', function (req, reply) {
    // Or this.mongo.client.db('mydb')
    const db = this.mongo.client.db('tomb')
    db.collection('defunct', onCollection)

    function onCollection(err, col) {
        if (err) return reply.send(err)
        console.log('req.params.id');
        console.log(req.params.id);
        col.findOne({
            id: req.params.id
        }, (err, user) => {
            console.log(user);
            reply.send(user)
        })
    }
})
fastify.post('/defunct', async function (req, reply) {
    try {
        let users = await this.mongo.db.collection('defunct').find(req.body).toArray();
        console.log(req.body);
        reply.send(users)
    } catch (e) {
        console.log(e.message);
        reply.status(400)
        reply.send(e.message)
    }
})





fastify.listen(3999, '0.0.0.0', err => {
    if (err) throw err
})