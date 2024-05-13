import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {useToast} from "@/components/ui/use-toast.ts";
import {isValidPhoneNumber} from "react-phone-number-input";
import {PhoneInput} from "@/components/ui/phone-input.tsx";

const formSchema = z.object({
    email: z.string().min(1, {message: "This field has to be filled."})
        .email("This is not a valid email."),
    login: z.string().min(2, {message: "Login has to be at least 2 characters long."})
        .max(32, {message: "Login has to be at most 32 characters long."}), //TODO add regex for login
    password: z.string().min(2, {message: "Password has to be at least 2 characters long."})
        .max(60, {message: "Password has to be at most 60 characters long."}),
    firstName: z.string().min(2, {message: "First name has to be at least 2 characters long."})
        .max(32, {message: "First name has to be at most 32 characters long."}),
    lastName: z.string().min(2, {message: "Last name has to be at least 2 characters long."})
        .max(32, {message: "Last name has to be at most 32 characters long."}),
    phoneNumber: z.string().refine(isValidPhoneNumber, {message: "Invalid phone number"}),
})

export function RegisterForm() {
    const {toast} = useToast()

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            login: "",
            password: "",
            email: "",
            firstName: "",
            lastName: "",
            phoneNumber: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        console.table(values)
        api.registerClient(values.login, values.password, values.firstName, values.lastName, values.email,
            values.phoneNumber, navigator.language.substring(0, 2))
            .then(() => {
                toast({
                    title: "Almost there!",
                    description: "Check the e-mail you provided in order to activate your Eldorado account.",
                });
            })
            .catch((error) => {
                toast({
                    variant: "destructive",
                    description: "Something went wrong. Please try again later.",
                })
                console.log(error.response.data)
            });
    }

    return (
        <Card className="mx-auto max-w-2xl">
            <CardHeader>
                <CardTitle className="text-2xl">Sign Up</CardTitle>
                <CardDescription>
                    Enter your information to create an account
                </CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                        <div className="grid gap-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="firstName"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel className="text-black">First Name</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="lastName"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel className="text-black">Last Name</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="phoneNumber"
                                        render={({field}) => (
                                            <FormItem className="items-start">
                                                <FormLabel className="text-black text-center">Phone Number</FormLabel>
                                                <FormControl className="w-full">
                                                    <PhoneInput //TODO fix this
                                                        placeholder="" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="email"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel className="text-black">E-mail</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="mail@example.com" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="grid gap-2">
                                <FormField
                                    control={form.control}
                                    name="login"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel className="text-black">Login</FormLabel>
                                            <FormControl>
                                                <Input placeholder="" {...field}/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="grid gap-2">
                                <FormField
                                    control={form.control}
                                    name="password"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel className="text-black">Password</FormLabel>
                                            <FormControl>
                                                <Input type="password" {...field}/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <Button type="submit" className="w-full">
                                Create an account
                            </Button>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            Already have an account?{" "}
                            <a href="/login" className="font-medium text-black hover:text-blue-500">
                                Log in </a>
                        </div>
                    </form>
                </Form>
            </CardContent>
        </Card>
    )
}

export default RegisterForm
