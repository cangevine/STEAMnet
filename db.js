// faux database

var sparks = exports.sparks = [];

sparks.push({ content: 'Holy cow! I just had a great idea.', id: 0 });
sparks.push({ content: 'Next project: fleet of FCS drones', id: 1 });
sparks.push({ content: 'I have an idea and I would like to share it here.', id: 2 });

var users = exports.users = [];

users.push({ name: 'TJ', sparks: [sparks[0], sparks[1], sparks[2]], id: 0  });
users.push({ name: 'Guillermo', sparks: [sparks[3]], id: 1 });
users.push({ name: 'Nathan', sparks: [], id: 2 });