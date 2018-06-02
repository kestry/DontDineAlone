using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine.Handlers
{
    public class MATCH : IHandler
    {
        public void Invoke(User u, Reader r)
        {
            string id = r.readStr();
            string name = r.readStr();
            if (String.IsNullOrEmpty(u.getId()))
                u.setId(id);
            u.setName(name);
            Console.WriteLine("id: " + u.getId() + " name: " + u.getName());
            int nine_ten = r.read8();
            int cowell_stevenson = r.read8();
            int crown_merrill = r.read8();
            int porter_kresge = r.read8();
            int rc_oakes = r.read8();
            Console.WriteLine("Nine/Ten: " + nine_ten + " Cowell/Stevenson: " + cowell_stevenson + " Crown/Merrill: " + crown_merrill + " Porter/Kresge: " + porter_kresge + "RC/Oakes: " + rc_oakes);
            MatchQuery matchQuery = new MatchQuery(u.getId());
            if (nine_ten == 1)
            {
                matchQuery.update("nine/ten");
                Console.WriteLine("here");
            }
            if (cowell_stevenson == 1)
                matchQuery.update("cowell/stevenson");
            if (crown_merrill == 1)
                matchQuery.update("crown/merill");
            if (porter_kresge == 1)
                matchQuery.update("porter/kresge");
            if (rc_oakes == 1)
                matchQuery.update("rc/oakes");


            string partnerId = Matching.tryMatch(matchQuery);

            if(String.IsNullOrEmpty(partnerId)) // No Match Found
            {
                Writer w = new Writer(0x02);
                w.write((byte)0);
                u.send(w);
                Console.WriteLine("Sent: " + BitConverter.ToString(w.getData()).Replace("-", " "));
            }
            else
            {
                User partner = Server.getUser(partnerId);

                if (partner == null)
                    return;

                string chatId = (u.getId() + "-" + partner.getId());
                Chat c = new Chat(chatId, u, partner);
                Messenger.add(chatId, c);

                Writer w = new Writer(0x02);
                w.write((byte)1);
                w.writeStr(partnerId);
                u.send(w);

                w = new Writer(0x02);
                w.write((byte)1);
                w.writeStr(u.getId());
                partner.send(w);
            }
        }
    }
}
